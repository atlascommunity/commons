/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira.history;

import lombok.Getter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

import static ru.mail.jira.plugins.commons.dto.jira.history.HistoryItemType.CHANGE;

@XmlRootElement
@Getter
public class ChangeDto extends HistoryItem {
  @XmlElement private final HistoryItemType itemType = CHANGE;
  @XmlElement private long id;
  @XmlElement private List<ChangeItemDto> items;
}
