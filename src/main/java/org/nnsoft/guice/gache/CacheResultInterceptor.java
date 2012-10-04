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

import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.CacheResult;

import org.aopalliance.intercept.MethodInvocation;

/**
 *
 */
final class CacheResultInterceptor
    extends CacheInterceptor<CacheResult>
{

    @Override
    public Class<CacheResult> getInterceptedAnnotationType()
    {
        return CacheResult.class;
    }

    @Override
    protected Object invoke( CacheInvocationContext<CacheResult> context, MethodInvocation invocation )
        throws Throwable
    {
        // TODO Auto-generated method stub
        return null;
    }

}
