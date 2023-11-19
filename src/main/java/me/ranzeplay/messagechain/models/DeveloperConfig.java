package me.ranzeplay.messagechain.models;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RangeConstraint;
import io.wispforest.owo.config.annotation.RestartRequired;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Modmenu(modId = "messagechain")
@Config(name = "msgc-dev-config", wrapperName = "MessageChainDeveloperConfig")
public class DeveloperConfig {
    @RestartRequired
    public boolean enableRoutingTest;
    @RestartRequired
    public boolean enableNotificationTest;

    @RangeConstraint(min = 0, max = 5000)
    public int timeoutMilliseconds;

    public DeveloperConfig() {
        enableNotificationTest = false;
        enableRoutingTest = false;
        timeoutMilliseconds = 2000;
    }
}
