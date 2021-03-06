/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.as.model;

import java.util.Collections;
import java.util.List;

/**
 * @author Emanuel Muckenhuber
 */
public class DomainPathRemove extends AbstractDomainModelUpdate<Void> {

    private static final long serialVersionUID = -4310868265566084137L;
    private final String name;

    public DomainPathRemove(String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    @Override
    protected void applyUpdate(DomainModel element) throws UpdateFailedException {
        if(! element.removePath(name)) {
            throw new UpdateFailedException(String.format("path (%s) does not exist", name));
        }
    }

    /** {@inheritDoc} */
    @Override
    public AbstractDomainModelUpdate<?> getCompensatingUpdate(DomainModel original) {
        final PathElement path = original.getPath(name);
        if(path == null) {
            return null;
        }
        return new DomainPathAdd(new PathElementUpdate(name, path.getPath(), path.getRelativeTo()));
    }

    /** {@inheritDoc} */
    @Override
    public AbstractServerModelUpdate<Void> getServerModelUpdate() {
        return new ServerPathRemove(name);
    }

    @Override
    public List<String> getAffectedServers(DomainModel domainModel, HostModel hostModel) throws UpdateFailedException {
        // requires a restart
        return Collections.emptyList();
    }

}
