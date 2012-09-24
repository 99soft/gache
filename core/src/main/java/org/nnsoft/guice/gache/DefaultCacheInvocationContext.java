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

import static java.util.Arrays.asList;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.CacheInvocationParameter;

import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.Injector;

final class DefaultCacheInvocationContext<A extends Annotation>
    implements CacheInvocationContext<A>
{

    private final Injector injector;

    private final MethodInvocation invocation;

    private final Class<A> interceptedAnnotationType;

    public DefaultCacheInvocationContext( Injector injector,
                                          MethodInvocation invocation,
                                          Class<A> interceptedAnnotationType )
    {
        this.injector = injector;
        this.invocation = invocation;
        this.interceptedAnnotationType = interceptedAnnotationType;
    }

    public Method getMethod()
    {
        return invocation.getMethod();
    }

    public Set<Annotation> getAnnotations()
    {
        return toAnnotationsSet( invocation.getMethod().getAnnotations() );
    }

    public A getCacheAnnotation()
    {
        return invocation.getMethod().getAnnotation( interceptedAnnotationType );
    }

    public String getCacheName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getTarget()
    {
        return invocation.getThis();
    }

    public CacheInvocationParameter[] getAllParameters()
    {
        CacheInvocationParameter[] parameters = new CacheInvocationParameter[invocation.getArguments().length];

        for ( int i = 0; i < invocation.getArguments().length; i++ )
        {
            parameters[i] = new DefaultCacheInvocationParameter( invocation.getMethod().getParameterTypes()[i],
                                                                 invocation.getArguments()[i],
                                                                 toAnnotationsSet( invocation.getMethod().getParameterAnnotations()[i] ),
                                                                 i );
        }

        return parameters;
    }

    public <T> T unwrap( Class<T> cls )
    {
        return injector.getInstance( cls );
    }

    private static Set<Annotation> toAnnotationsSet( Annotation...annotations )
    {
        return new HashSet<Annotation>( asList( annotations ) );
    }

}
