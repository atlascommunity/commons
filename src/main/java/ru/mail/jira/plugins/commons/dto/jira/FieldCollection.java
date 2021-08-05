/* (C)2020 */
package ru.mail.jira.plugins.commons.dto.jira;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@XmlRootElement
public class FieldCollection {
  @XmlElement private List<FieldDto> other;
  @XmlElement private List<FieldDto> users;
  @XmlElement private List<FieldDto> dates;
}
