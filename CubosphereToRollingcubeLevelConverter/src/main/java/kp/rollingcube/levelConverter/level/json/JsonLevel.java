package kp.rollingcube.levelConverter.level.json;

import kp.rollingcube.levelConverter.utils.Version;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 *
 * @author Marc
 */
@Data
@NoArgsConstructor
public class JsonLevel
{
    private @NonNull String name = "";
    private @NonNull Version version = Version.zero();
}
