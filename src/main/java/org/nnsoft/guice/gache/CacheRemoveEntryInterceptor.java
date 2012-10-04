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

import javax.cache.Cache;
import javax.cache.annotation.CacheInvocationContext;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheKeyInvocationContext;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheRemoveEntry;

/**
 *
 */
final class CacheRemoveEntryInterceptor
    extends AfterBeforeInvocationInterceptor<CacheRemoveEntry>
{

    @Override
    public Class<CacheRemoveEntry> getInterceptedAnnotationType()
    {
        return CacheRemoveEntry.class;
    }

    @Override
    protected void hitCache( CacheInvocationContext<CacheRemoveEntry> context )
    {
        CacheKeyInvocationContext<CacheRemoveEntry> keyedContext = (CacheKeyInvocationContext<CacheRemoveEntry>) context;

        Cache<Object, Object> cache = getCacheResolverFactory( context ).getCacheResolver( context ).resolveCache( context );
        CacheKey cacheKey = getCacheKeyGenerator( context ).generateCacheKey( keyedContext );

        cache.remove( cacheKey );
    }

}
