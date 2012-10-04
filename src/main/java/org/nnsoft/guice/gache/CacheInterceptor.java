package org.nnsoft.guice.gache;

/*
 *  Copyright 2012 The 99 Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import static com.google.inject.internal.util.$Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.cache.annotation.CacheAnnotationConfigurationException;
import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyGenerator;
import javax.cache.annotation.CacheKeyParam;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheRemoveEntry;
import javax.cache.annotation.CacheResolver;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import javax.inject.Inject;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Injector;

abstract class CacheInterceptor<A extends Annotation>
    implements MethodInterceptor
{

    @Inject
    private Injector injector;

    @Inject
    private CacheResolver cacheResolver;

    @Inject
    private CacheKeyGenerator cacheKeyGenerator;

    protected final Injector getInjector()
    {
        return injector;
    }

    public final void setInjector( Injector injector )
    {
        this.injector = injector;
    }

    protected final CacheResolver getCacheResolver()
    {
        return cacheResolver;
    }

    public void setCacheResolver( CacheResolver cacheResolver )
    {
        this.cacheResolver = cacheResolver;
    }

    protected final CacheKeyGenerator getCacheKeyGenerator()
    {
        return cacheKeyGenerator;
    }

    public final void setCacheKeyGenerator( CacheKeyGenerator cacheKeyGenerator )
    {
        this.cacheKeyGenerator = cacheKeyGenerator;
    }

    public abstract Class<A> getInterceptedAnnotationType();

    public final Object invoke( MethodInvocation invocation )
        throws Throwable
    {
        String cacheName = getCacheName( invocation.getMethod() );
        Object target = invocation.getThis();
        A interceptedAnnotation = invocation.getMethod().getAnnotation( getInterceptedAnnotationType() );
        Set<Annotation> methodAnnotations = toAnnotationsSet( invocation.getMethod().getAnnotations() );

        boolean cacheValueAllowed = CachePut.class == getInterceptedAnnotationType();

        CacheInvocationParameter[] allParameters = new CacheInvocationParameter[invocation.getArguments().length];
        List<CacheInvocationParameter> keyParametersList = new ArrayList<CacheInvocationParameter>( allParameters.length );
        CacheInvocationParameter valueParameter = null;

        for ( int i = 0; i < invocation.getArguments().length; i++ )
        {
            Class<?> parameterType = invocation.getMethod().getParameterTypes()[i];

            CacheInvocationParameter parameter = new DefaultCacheInvocationParameter( parameterType,
                                                                                      invocation.getArguments()[i],
                                                                                      toAnnotationsSet( invocation.getMethod().getParameterAnnotations()[i] ),
                                                                                      i );

            for ( Annotation parameterAnnotation : invocation.getMethod().getParameterAnnotations()[i] )
            {
                if ( CacheKeyParam.class == parameterAnnotation.annotationType() )
                {
                    keyParametersList.add( parameter );
                }
                else if ( CacheValue.class == parameterAnnotation.annotationType() )
                {
                    if ( !cacheValueAllowed )
                    {
                        throw new CacheAnnotationConfigurationException( format( "CacheValue parameter annotation is not allowed on %s",
                                                                                 invocation.getMethod() ) );
                    }
                    else if ( valueParameter != null )
                    {
                        throw new CacheAnnotationConfigurationException( format( "Multiple CacheValue parameter annotations are not allowed on %s",
                                                                                 invocation.getMethod() ) );
                    }
                    else
                    {
                        valueParameter = parameter;
                    }
                }
            }

            allParameters[i] = parameter;
        }

        CacheInvocationParameter[] keyParameters;

        if ( keyParametersList.isEmpty() )
        {
            keyParameters = allParameters;
        }
        else
        {
            keyParameters = keyParametersList.toArray( new CacheInvocationParameter[keyParametersList.size()] );
        }

        return invoke( new DefaultCacheKeyInvocationContext<A>( injector,
                                                                cacheName,
                                                                target,
                                                                invocation.getMethod(),
                                                                allParameters,
                                                                keyParameters,
                                                                valueParameter,
                                                                methodAnnotations,
                                                                interceptedAnnotation ),
                       invocation );
    }

    protected abstract Object invoke( CacheInvocationContext<A> context, MethodInvocation invocation )
        throws Throwable;

    @SuppressWarnings( "unchecked" )
    private static String getCacheName( Method method )
    {
        for ( Class<? extends Annotation> annotationType : asList( CachePut.class,
                                                                   CacheRemoveAll.class,
                                                                   CacheRemoveEntry.class,
                                                                   CacheResult.class ) )
        {
            if ( method.isAnnotationPresent( annotationType ) )
            {
                Annotation annotation = method.getAnnotation( annotationType );
                String cacheName;
                try
                {
                    cacheName = (String) annotationType.getMethod( "cacheName" ).invoke( annotation );
                }
                catch ( Exception e )
                {
                    // should not happen, all enlisted annotations have "cacheName()" method
                    cacheName = null;
                }

                if ( !isEmpty( cacheName ) )
                {
                    return cacheName;
                }
            }
        }

        if ( method.getDeclaringClass().isAnnotationPresent( CacheDefaults.class ) )
        {
            CacheDefaults cacheDefaults = method.getDeclaringClass().getAnnotation( CacheDefaults.class );
            if ( !isEmpty( cacheDefaults.cacheName() ) )
            {
                return cacheDefaults.cacheName();
            }
        }

        return method.toGenericString();
    }

    static <T extends Throwable> boolean include( T throwable,
                                                  Class<? extends T>[] includes,
                                                  Class<? extends T>[] excludes,
                                                  boolean includeBothEmpty )
    {
        boolean includedEmpty = isEmpty( includes );
        boolean excludedEmpty = isEmpty( excludes );

        if ( includedEmpty && excludedEmpty )
        {
            return includeBothEmpty;
        }

        boolean isAssignableFromIncludes = isAssignable( throwable, includes );
        boolean isAssignableFromExcludes = isAssignable( throwable, excludes );

        if ( includedEmpty )
        {
            return !isAssignableFromExcludes;
        }

        if ( excludedEmpty )
        {
            return isAssignableFromIncludes;
        }

        return isAssignableFromIncludes && !isAssignableFromExcludes;
    }

    private static final <T> boolean isEmpty( Class<? extends T>...types )
    {
        return types == null || types.length == 0;
    }

    private static boolean isEmpty( String value )
    {
        return value == null || value.length() == 0;
    }

    private static Set<Annotation> toAnnotationsSet( Annotation...annotations )
    {
        return unmodifiableSet( new LinkedHashSet<Annotation>( asList( annotations ) ) );
    }

    /**
     * Determines if a candidate object's type matches an element in the classes array.
     *
     * @param target The object to check if its type matches one of the classes in the array, must not be null.
     * @param from List of classes to check against, may be null.
     * @return null if classes array is null or if candidate is not an instanceof any member of the classes array.
     */
    private static <T> boolean isAssignable( T target, Class<? extends T>[] from )
    {
        if ( from == null )
        {
            return false;
        }

        final Class<? extends Object> candidateClass = target.getClass();
        for ( final Class<? extends T> throwable : from )
        {
            if ( throwable.isAssignableFrom( candidateClass ) )
            {
                return true;
            }
        }

        return false;
    }

}
