/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class WatchersDto {
  @XmlElement private List<UserDto> watchers;
  @XmlElement private boolean isWatching;
  @XmlElement private int watcherCount;
}
