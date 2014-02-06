/*
 * Copyright (C) 2005-2014 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.po.rm;

import org.alfresco.po.share.site.CreateSitePage;
import org.alfresco.webdrone.HtmlPage;
import org.alfresco.webdrone.RenderTime;
import org.alfresco.webdrone.WebDrone;
import org.alfresco.webdrone.WebDroneUtil;
import org.alfresco.webdrone.exception.PageOperationException;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * Extends {@link CreateSitePage} in order to add RM specific methods
 *
 * @author Tuna Aksoy
 * @author Roy Wetherall
 * @since 2.2
 */
public class RmCreateSitePage extends CreateSitePage
{
    /** Default site details */
    public static final String RM_SITE_NAME = "Records Management";
    public static final String RM_SITE_DESC = "Records Management Site";
    public static final String RM_SITE_URL = "rm";

    /** RM site compliance */
    public enum RMSiteCompliance
    {
        STANDARD,
        DOD5015
    }

    private static final By SITE_PRESET = By.id("alfresco-rm-createSite-instance-sitePreset");

    /** page controls */
    public static final By SELECT_COMPLIANCE = By.name("compliance");

    /**
     * Constructor.
     *
     * @param drone {@link WebDrone}
     */
    public RmCreateSitePage(WebDrone drone)
    {
        super(drone);
    }

    /**
     * @see org.alfresco.po.share.site.CreateSitePage#render(org.alfresco.webdrone.RenderTime)
     */
    @SuppressWarnings("unchecked")
    @Override
    public RmCreateSitePage render(RenderTime timer)
    {
        WebDroneUtil.checkMandotaryParam("timer", timer);

        return (RmCreateSitePage) super.render(timer);
    }

    /**
     * @see org.alfresco.po.share.site.CreateSitePage#render()
     */
    @SuppressWarnings("unchecked")
    @Override
    public RmCreateSitePage render()
    {
        return (RmCreateSitePage) super.render();
    }

    /**
     * @see org.alfresco.po.share.site.CreateSitePage#render(long)
     */
    @SuppressWarnings("unchecked")
    @Override
    public RmCreateSitePage render(final long time)
    {
        return (RmCreateSitePage) super.render(time);
    }

    /**
     * Create a new public site action.
     *
     * @param siteName String mandatory field
     * @return {@link HtmlPage} page response
     */
    public HtmlPage createRMSite(RMSiteCompliance compliance)
    {
        // setup the type and compliance
        selectSiteType(RMSiteType.RECORDS_MANAGEMENT);
        selectRMSiteCompliance(compliance);

        // FIXME!!!
        return createNewSite(null, null, false, false, RMSiteType.RECORDS_MANAGEMENT);
    }

    /**
     * @see org.alfresco.po.share.site.CreateSitePage#createSite(java.lang.String, java.lang.String, java.lang.String)
     */
    protected HtmlPage createSite(final String siteName, final String description, final String siteType)
    {
        switch (siteType)
        {
            case RMSiteType.RECORDS_MANAGEMENT:
                drone.find(SUBMIT_BUTTON).click();
                return new RmDashBoardPage(drone);

            default:
                return super.createSite(siteName, description, siteType);
        }
    }

    /**
     * @see org.alfresco.po.share.site.CreateSitePage#selectSiteType(java.lang.String)
     */
    public void selectSiteType(String siteType)
    {
        WebDroneUtil.checkMandotaryParam("siteType", siteType);

        Select dropdown = new Select(drone.find(SITE_PRESET));
        switch (siteType)
        {
            case RMSiteType.RECORDS_MANAGEMENT:
                dropdown.selectByIndex(1);
                break;

            case RMSiteType.COLLABORATION:
                dropdown.selectByIndex(0);
                break;

            default:
                throw new PageOperationException("No suitable site type was found");
        }
    }

    /**
     * @return  site type
     */
    public String getSiteType()
    {
        String result = RMSiteType.COLLABORATION;

        Select select = new Select(drone.find(SITE_PRESET));
        String selectedValue = select.getFirstSelectedOption().getAttribute("value");

        if (selectedValue.contains("rm-site-dashboard") == true)
        {
            result = RMSiteType.RECORDS_MANAGEMENT;
        }

        return result;
    }

    /**
     * @param compliance    site compliance
     */
    public void selectRMSiteCompliance(RMSiteCompliance compliance)
    {
        WebDroneUtil.checkMandotaryParam("compliance", compliance);

        Select dropdown = new Select(drone.find(SELECT_COMPLIANCE));
        switch (compliance)
        {
            case STANDARD:
                dropdown.selectByIndex(0);
                break;

            case DOD5015:
                dropdown.selectByIndex(1);
                break;

            default:
                throw new PageOperationException("No suitable compliance was found");
        }

    }

    /**
     * @return  site compliance value
     */
    public RMSiteCompliance getSiteCompliance()
    {
        RMSiteCompliance result = RMSiteCompliance.STANDARD;

        Select select = new Select(drone.find(SELECT_COMPLIANCE));
        String selectedValue = select.getFirstSelectedOption().getAttribute("value");

        if (selectedValue.contains("dod") == true)
        {
            result = RMSiteCompliance.DOD5015;
        }

        return result;
    }

    /**
     * Checks if drop down contains option for records management.
     * This can be confirmed by checking the dropdown id.
     *
     * @return <code>true</code> if id matches alfresco-rm <code>false</code> otherwise
     */
    public boolean isRecordManagementTypeSupported()
    {
        // TODO .. not sure this is doing the correct check!

        try
        {
            WebElement sitePreset = drone.find(SITE_PRESET);
            return sitePreset.isDisplayed();
        }
        catch (NoSuchElementException e)
        {
        }
        return false;
    }
}