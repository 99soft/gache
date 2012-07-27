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

import static java.lang.String.format;
import static org.nnsoft.guice.gache.Cached.DEFAULT_ID;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;

/**
 *
 */
final class CachedInterceptor
    extends CacheInterceptor
{

    public Object invoke( MethodInvocation invocation )
        throws Throwable
    {
        final Method invokedMethod = invocation.getMethod();

        // we can be sure about that because of the interceptor
        final Cached cached = invokedMethod.getAnnotation( Cached.class );

        final String cacheEntryId;

        if ( !DEFAULT_ID.equals( cached.id() ) )
        {
            cacheEntryId = cached.id();
        }
        else
        {
            cacheEntryId = format( "%s#%s(%s)", invokedMethod.getDeclaringClass(), invokedMethod.getName(), Arrays.toString( invokedMethod.getParameterTypes() ) );
        }

        final CacheKey cacheKey = new CacheKey( cacheEntryId, invokedMethod, invocation.getArguments() );

        return null;
    }

}
