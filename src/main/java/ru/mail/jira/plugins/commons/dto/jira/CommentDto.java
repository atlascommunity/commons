package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class CommentDto {
  @XmlElement private Long id;
  @XmlElement private String body;
  @XmlElement private String htmlBody;
  @XmlElement private UserDto author;
  @XmlElement private String updated;
  @XmlElement private UserDto currentUser;
  @XmlElement private Long issueId;
  @XmlElement private boolean updateOwnPermission;
  @XmlElement private boolean updateAllPermission;
  @XmlElement private boolean deleteOwnPermission;
  @XmlElement private boolean deleteAllPermission;
}
