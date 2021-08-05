/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class IssueDto {
  @XmlElement private Long id;
  @XmlElement private String key;
  @XmlElement private IssueTypeDto issueType;
  @XmlElement private PriorityDto priority;
  @XmlElement private IssueDto parent;
  @XmlElement private String title;
  @XmlElement private String description;
  @XmlElement private String statusId;
  @XmlElement private String status;
  @XmlElement private String statusColor;
  @XmlElement private UserDto reporter;
  @XmlElement private UserDto assignee;
  @XmlElement private String created;
  @XmlElement private String updated;
  @XmlElement private boolean resolution;
  @XmlElement private String resolutionName;
  @XmlElement private boolean browsePermission;
  @XmlElement private boolean editPermission;
  @XmlElement private boolean viewWatchersPermission;
  @XmlElement private boolean manageWatchersPermission;
  @XmlElement private boolean attachFilesPermission;
  @XmlElement private boolean linkPermission;
}
