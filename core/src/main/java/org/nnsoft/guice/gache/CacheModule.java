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

import java.lang.annotation.Annotation;

import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemoveAll;
import javax.cache.annotation.CacheRemoveEntry;
import javax.cache.annotation.CacheResult;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.inject.AbstractModule;

public abstract class CacheModule
    extends AbstractModule
{

    @Override
    protected final void configure()
    {
        configureCache();

        bindInterceptor( CachePut.class, new CachePutInterceptor() );
        bindInterceptor( CacheResult.class, new CacheResultInterceptor() );
        bindInterceptor( CacheRemoveEntry.class, new CacheRemoveEntryInterceptor() );
        bindInterceptor( CacheRemoveAll.class, new CacheRemoveAllInterceptor() );
    }

    protected abstract void configureCache();

    private <A extends Annotation> void bindInterceptor( Class<A> annotationType, MethodInterceptor interceptor )
    {
        bindInterceptor( any(), annotatedWith( annotationType ), interceptor );
    }

}
