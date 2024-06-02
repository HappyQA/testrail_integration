import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ICredentialFile extends Config {

    @Key("TestRailURL")
    String testRailUrl();

    @Key("TestRailLogin")
    String testRailLogin();

    @Key("TestRailPassword")
    String testRailPassword();
}
