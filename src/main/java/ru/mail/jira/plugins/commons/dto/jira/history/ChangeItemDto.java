/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira.history;

import lombok.Getter;
import lombok.Setter;
import ru.mail.jira.plugins.commons.dto.jira.UserDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement
public class ChangeItemDto {
  @XmlElement private String from;
  @XmlElement private String to;

  @XmlElement private ChangeType type = ChangeType.GENERIC;
  @XmlElement private String field;
  @XmlElement private UserDto user;
  @XmlElement private String statusColor;

  public enum ChangeType {
    ASSIGNEE,
    STATUS,
    DESCRIPTION,
    GENERIC
  }
}
