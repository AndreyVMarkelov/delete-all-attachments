/*
 * Created by Andrey Markelov 26-10-2012.
 * Copyright Mail.Ru Group 2012. All rights reserved.
 */
package ru.mail.jira.plugins;

import java.util.Collection;
import com.atlassian.jira.ComponentManager;
import com.atlassian.jira.exception.RemoveException;
import com.atlassian.jira.issue.AttachmentManager;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.issue.attachment.Attachment;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.web.action.issue.AbstractIssueSelectAction;
import com.atlassian.jira.webtests.Permissions;

/**
 * Delete all attachments action.
 * 
 * @author Andrey Markelov
 */
@SuppressWarnings("unchecked")
public class DeleteAttachAction
    extends AbstractIssueSelectAction
{
    /**
     * Unique ID.
     */
    private static final long serialVersionUID = 2431906489689708128L;

    /**
     * Plug-In data manager.
     */
    private final AttacherMgr attacherMgr;

    /**
     * Constructor.
     */
    public DeleteAttachAction(
        AttacherMgr attacherMgr)
    {
        this.attacherMgr = attacherMgr;
    }

    @Override
    @com.atlassian.jira.security.xsrf.RequiresXsrfCheck
    protected String doExecute()
    throws Exception
    {
        MutableIssue issue = getIssueObject();
        if (!hasPermission(issue.getProjectObject()))
        {
            addErrorMessage(getText("attachdelete.delete.error.permission"));
            return ERROR;
        }

        Collection<Attachment> atts = issue.getAttachments();
        AttachmentManager am = ComponentManager.getInstance().getAttachmentManager();
        for (Attachment att : atts)
        {
            try
            {
                am.deleteAttachment(att);
            }
            catch (RemoveException e)
            {
                addErrorMessage(getText("attachdelete.delete.error.file", att.getFilename()));
                return ERROR;
            }
        }

        return getRedirect("/browse/" + issue.getKey());
    }

    /**
     * Check permission.
     */
    private boolean hasPermission(Project project)
    {
        if (project != null)
        {
            String[] projectKeys = attacherMgr.getProjectKeys();
            if (projectKeys != null)
            {
                for (String projectKey : projectKeys)
                {
                    if (projectKey.equals(project.getId().toString()))
                    {
                        return getPermissionManager().hasPermission(Permissions.ATTACHMENT_DELETE_ALL, project, getLoggedInUser());
                    }
                }
            }
        }

        return false;
    }
}
