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

/**
 * Understands an unrecoverable error encountered in the cache package.
 */
public class FatalCacheException
    extends CacheException
{

    private static final long serialVersionUID = -5778844057206602130L;

    public FatalCacheException( String messagePattern, Object... args )
    {
        super( messagePattern, args );
    }

    public FatalCacheException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
