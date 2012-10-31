/*
 * Created by Andrey Markelov 26-10-2012.
 * Copyright Mail.Ru Group 2012. All rights reserved.
 */
package ru.mail.jira.plugins;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.atlassian.crowd.embedded.api.User;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.project.ProjectManager;
import com.atlassian.jira.security.Permissions;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.sal.api.ApplicationProperties;

/**
 * Configure Plug-In action.
 * 
 * @author Andrey Markelov
 */
public class AttachDeleterConfig
    extends JiraWebActionSupport
{
    /**
     * Unique ID.
     */
    private static final long serialVersionUID = -8881690781888724545L;

    /**
     * Application properties.
     */
    private final ApplicationProperties applicationProperties;

    /**
     * Plug-In data manager.
     */
    private final AttacherMgr attacherMgr;

    /**
     * Is saved?
     */
    private boolean isSaved = false;

    /**
     * Project manager.
     */
    private ProjectManager prMgr;

    /**
     * Saved projects.
     */
    private List<String> savedProjKeys;

    /**
     * Selected projects.
     */
    private String[] selectedProjKeys = new String[0];

    /**
     * Constructor.
     */
    public AttachDeleterConfig(
        ApplicationProperties applicationProperties,
        AttacherMgr attacherMgr,
        ProjectManager prMgr)
    {
        this.applicationProperties = applicationProperties;
        this.attacherMgr = attacherMgr;
        this.prMgr = prMgr;

        selectedProjKeys = attacherMgr.getProjectKeys();
        savedProjKeys = selectedProjKeys == null ? null : Arrays.asList(selectedProjKeys);
    }

    @Override
    protected String doExecute()
    throws Exception
    {
        attacherMgr.setProjectKeys(selectedProjKeys);
        if (selectedProjKeys != null)
        {
            savedProjKeys = Arrays.asList(attacherMgr.getProjectKeys());
        }
        setSaved(true);

        return getRedirect("AttachDeleterConfig!default.jspa?saved=true");
    }

    public Map<String, String> getAllProjects()
    {
        Map<String, String> allProjs = new TreeMap<String, String>();

        List<Project> projs = prMgr.getProjectObjects();
        if (projs != null)
        {
            for (Project proj : projs)
            {
                allProjs.put(proj.getId().toString(), getProjView(proj.getName(), proj.getDescription()));
            }
        }

        return allProjs;
    }

    /**
     * Get context path.
     */
    public String getBaseUrl()
    {
        return applicationProperties.getBaseUrl();
    }

    private String getProjView(String name, String descr)
    {
        if (descr != null && !descr.isEmpty())
        {
            return (name + ": " + descr);
        }

        return name;
    }

    public List<String> getSavedProjKeys()
    {
        return savedProjKeys;
    }

    public String[] getSelectedProjKeys()
    {
        return selectedProjKeys;
    }

    public boolean hasAdminPermission()
    {
        User user = getLoggedInUser();
        if (user == null)
        {
            return false;
        }

        return getPermissionManager().hasPermission(Permissions.ADMINISTER, getLoggedInUser());
    }

    public boolean isSaved()
    {
        return isSaved;
    }

    public void setSaved(boolean isSaved)
    {
        this.isSaved = isSaved;
    }

    public void setSavedProjKeys(List<String> savedProjKeys)
    {
        this.savedProjKeys = savedProjKeys;
    }

    public void setSelectedProjKeys(String[] selectedProjKeys)
    {
        this.selectedProjKeys = selectedProjKeys;
    }
}
