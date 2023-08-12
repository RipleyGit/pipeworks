package site.bleem.pipeworks.logger;

/**
 * The interface Message builder.
 * @author WestonHan
 */
@FunctionalInterface
public interface MessageBuilder{
    /**
     * Builder message string.
     *
     * @return the string
     */
    String builderMessage();
}