/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira.history;

import lombok.Getter;
import ru.mail.jira.plugins.commons.dto.jira.UserDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@XmlRootElement
public class CommentDto extends HistoryItem {
  @XmlElement private final HistoryItemType itemType = HistoryItemType.COMMENT;
  @XmlElement private Long id;
  @XmlElement private String body;
  @XmlElement private String htmlBody;
  @XmlElement private UserDto currentUser;
  @XmlElement private Long issueId;
  @XmlElement private String restriction;
  @XmlElement private boolean editable;
  @XmlElement private boolean deletable;
}
