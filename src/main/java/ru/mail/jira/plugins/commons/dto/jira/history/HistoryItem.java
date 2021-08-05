/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira.history;

import lombok.Getter;
import lombok.Setter;
import ru.mail.jira.plugins.commons.dto.jira.UserDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Getter
@Setter
public abstract class HistoryItem {
  @XmlElement private UserDto author;
  @XmlElement private String updated;
  @XmlElement private String type;
  private long timestamp;
}
