package org.mongodb.binding;

import org.mongodb.ReadPreference;
import org.mongodb.connection.Cluster;
import org.mongodb.connection.Connection;
import org.mongodb.connection.Server;
import org.mongodb.selector.PrimaryServerSelector;
import org.mongodb.selector.ReadPreferenceServerSelector;
import org.mongodb.selector.ServerSelector;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A simple ReadWriteBinding implementation that supplies write connection sources bound to a possibly different primary each time and a
 * read connection source bound to a possible different server each time.
 *
 * @since 3.0
 */
public class ClusterBinding extends AbstractReferenceCounted implements ReadWriteBinding {
    private final Cluster cluster;
    private final ReadPreference readPreference;
    private final long maxWaitTimeMS;

    public ClusterBinding(final Cluster cluster, final ReadPreference readPreference, final long maxWaitTime, final TimeUnit timeUnit) {
        this.cluster = cluster;
        this.readPreference = readPreference;
        this.maxWaitTimeMS = MILLISECONDS.convert(maxWaitTime, timeUnit);
    }

    @Override
    public ReadWriteBinding retain() {
        super.retain();
        return this;
    }

    @Override
    public ReadPreference getReadPreference() {
        return readPreference;
    }

    @Override
    public ConnectionSource getReadConnectionSource() {
        return new MyConnectionSource(new ReadPreferenceServerSelector(readPreference));
    }

    @Override
    public ConnectionSource getWriteConnectionSource() {
        return new MyConnectionSource(new PrimaryServerSelector());
    }

    private final class MyConnectionSource extends AbstractReferenceCounted implements ConnectionSource {
        private final Server server;

        private MyConnectionSource(final ServerSelector serverSelector) {
            this.server = cluster.selectServer(serverSelector, maxWaitTimeMS, MILLISECONDS);
            ClusterBinding.this.retain();
        }

        @Override
        public Connection getConnection() {
            return server.getConnection();
        }

        public ConnectionSource retain() {
            super.retain();
            ClusterBinding.this.retain();
            return this;
        }

        @Override
        public void release() {
            super.release();
            ClusterBinding.this.release();
        }
    }
}