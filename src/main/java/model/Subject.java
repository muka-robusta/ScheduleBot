package model;

import lombok.*;
import res.SubjectType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subject {
    private int id;
    private String name;
    private String link;
    private String tutorName;

    public static Subject getByValues(int id, String name, String link, String tutorName)
    {
        final Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        subject.setLink(link);
        subject.setTutorName(tutorName);

        return subject;
    }

}
