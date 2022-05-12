package site.pushy.landlords.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Pushy
 * @since 2019/1/1 17:30
 */
@AllArgsConstructor
@Getter
public enum IdentityEnum {

    LANDLORD("地主"),

    FARMER("农民");

    private final String name;
}
