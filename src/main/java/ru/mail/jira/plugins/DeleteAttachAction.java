/*
 * Created by Andrey Markelov 26-10-2012.
 * Copyright Mail.Ru Group 2012. All rights reserved.
 */
package ru.mail.jira.plugins;

import java.util.Collection;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.exception.RemoveException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.web.action.issue.AbstractIssueSelectAction;

/**
 * Delete all attachments action.
 * 
 * @author Andrey Markelov
 */
public class DeleteAttachAction
    extends AbstractIssueSelectAction
{
    /**
     * Unique ID.
     */
    private static final long serialVersionUID = 2431906489689708128L;

    /**
     * Issue manager.
     */
    private final IssueManager issueMgr;

    /**
     * Permission manager.
     */
    private final PermissionManager permMgr;

    /**
     * Constructor.
     */
    public DeleteAttachAction(
        IssueManager issueMgr,
        PermissionManager permMgr)
    {
        this.issueMgr = issueMgr;
        this.permMgr = permMgr;
    }

    @Override
    public String doDefault()
    {
        String issueKey = getIssueObject().getKey();

        MutableIssue d = issueMgr.getIssueObject(issueKey);
        Collection<Attachment> atts = d.getAttachments();
        AttachmentManager am = ComponentManager.getInstance().getAttachmentManager();
        for (Attachment att : atts)
        {
            try
            {
                am.deleteAttachment(att);
            }
            catch (RemoveException e)
            {
                
            }
        }

        return getRedirect("/browse/" + issueKey);
    }
}
