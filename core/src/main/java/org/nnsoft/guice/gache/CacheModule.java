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

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import com.google.inject.AbstractModule;

public abstract class CacheModule
    extends AbstractModule
{

    @Override
    protected final void configure()
    {
        configureCache();

        for ( CacheInterceptor interceptor : new CacheInterceptor[] { new CachePutInterceptor(),
                                                                      new CacheResultInterceptor(),
                                                                      new CacheRemoveEntryInterceptor(),
                                                                      new CacheRemoveAllInterceptor() } )
        {
            requestInjection( interceptor );
            bindInterceptor( any(), annotatedWith( interceptor.getInterceptedAnnotationType() ), interceptor );
        }
    }

    protected abstract void configureCache();

}
