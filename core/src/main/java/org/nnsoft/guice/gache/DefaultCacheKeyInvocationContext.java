package org.nnsoft.guice.gache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import javax.cache.annotation.CacheInvocationParameter;
import javax.cache.annotation.CacheKeyInvocationContext;

import com.google.inject.Injector;

final class DefaultCacheKeyInvocationContext<A extends Annotation>
    implements CacheKeyInvocationContext<A>
{

    private final Injector injector;

    protected final Method method;

    private final Set<Annotation> methodAnnotations;

    private final A interceptedAnnotation;

    private final String cacheName;

    public DefaultCacheKeyInvocationContext( Injector injector,
                                             Method method,
                                             Set<Annotation> methodAnnotations,
                                             A interceptedAnnotation,
                                             String cacheName )
    {
        this.injector = injector;
        this.method = method;
        this.methodAnnotations = methodAnnotations;
        this.interceptedAnnotation = interceptedAnnotation;
        this.cacheName = cacheName;
    }

    public final Method getMethod()
    {
        return method;
    }

    public final Set<Annotation> getAnnotations()
    {
        return methodAnnotations;
    }

    public final A getCacheAnnotation()
    {
        return interceptedAnnotation;
    }

    public final String getCacheName()
    {
        return cacheName;
    }

    public final Object getTarget()
    {
        return null;
    }

    public final CacheInvocationParameter[] getAllParameters()
    {
        return null;
    }

    public final <T> T unwrap( Class<T> cls )
    {
        return injector.getInstance( cls );
    }

    public CacheInvocationParameter[] getKeyParameters()
    {
        /* List<CacheInvocationParameter> parameters = new ArrayList<CacheInvocationParameter>( invocation.getArguments().length );

        for ( int i = 0; i < invocation.getArguments().length; i++ )
        {
            Class<?> parameterType = invocation.getMethod().getParameterTypes()[i];

            boolean keyFound = false;
            boolean valueFound = false;

            for ( Annotation parameterAnnotation : invocation.getMethod().getParameterAnnotations()[i] )
            {

            }
        }

        return parameters.toArray( new CacheInvocationParameter[parameters.size()] ); */
        return null;
    }

    public CacheInvocationParameter getValueParameter()
    {
        return null;
    }

}
