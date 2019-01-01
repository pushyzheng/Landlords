package site.pushy.landlords.core.enums;

/**
 * @author Pushy
 * @since 2019/1/1 17:30
 */
public enum IdentityEnum {

    LANDLORD("地主"),
    FARMER("农民");

    private String name;

    IdentityEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
