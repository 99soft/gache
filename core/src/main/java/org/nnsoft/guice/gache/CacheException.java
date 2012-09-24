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

/**
 * Understands an unexpected error that occurred while accessing a cache.
 */
public abstract class CacheException
    extends RuntimeException
{

    private static final long serialVersionUID = 1L;

    public CacheException( String messagePattern, Object...args )
    {
        this( format( messagePattern, args ), (Throwable) null );
    }

    public CacheException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
