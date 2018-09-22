/*
 * Copyright (c) 2018 Bull/Atos.
 * All rights reserved.
 */
package bull.internal.lines;

/**
 * [Enter type description here].
 *
 * @author Bull/Atos
 */
public abstract class RedmineIssueLine {
    /*Members*/
    protected String id;
    protected String subject;
    protected String createdOn;

    RedmineIssueLine(final String id, final String subject, final String createdOn) {
        this.id = id;
        this.subject = subject;
        this.createdOn = createdOn;
    }
}
