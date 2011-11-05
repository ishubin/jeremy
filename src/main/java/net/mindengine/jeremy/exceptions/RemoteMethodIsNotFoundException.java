/*******************************************************************************
 * 2011 Ivan Shubin http://mindengine.net
 * 
 * This file is part of Mind-Engine Jeremy.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Mind-Engine Jeremy.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.mindengine.jeremy.exceptions;

public class RemoteMethodIsNotFoundException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -1447044000409378563L;

    public RemoteMethodIsNotFoundException() {
        super();
    }

    public RemoteMethodIsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteMethodIsNotFoundException(String message) {
        super(message);
    }

    public RemoteMethodIsNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
