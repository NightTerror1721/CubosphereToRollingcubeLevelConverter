package kp.rollingcube.levelConverter.level;

import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import java.util.List;

public enum EnemyInteractionType
{
    SPIKES(0),
    LASERS(1),
    TELEPORTS(2),
    BUTTONS(3),
    LIGHT_SENSORS(4),
    DIRECTION_RESTRICTIONS(5),
    ICE(6),
    FIRE(7),
    BREAKING_BLOCKS(8),
    TRAMPS_AND_VENTS(9),
    MAGNETS(10),
    ROTATORS(11),
    BALL_SHIELD(12),
    INVISIBLE_BLOCKS(13);

    @Getter private final int code;

    EnemyInteractionType(int code)
    {
        this.code = code;
    }

    public int getFlag() { return 0b1 << code; }

    public static int set(int base, @NonNull EnemyInteractionType type) { return base | type.getFlag(); }
    public static int set(int base, @NonNull EnemyInteractionType @NonNull ... types) { return set(base, List.of(types)); }
    public static int set(int base, @NonNull Collection<EnemyInteractionType> types) {
        for (var type : types)
            base |= type.getFlag();
        return base;
    }

    public static int clear(int base, @NonNull EnemyInteractionType type) { return base & ~type.getFlag(); }
    public static int clear(int base, @NonNull EnemyInteractionType @NonNull ... types) { return clear(base, List.of(types)); }
    public static int clear(int base, @NonNull Collection<EnemyInteractionType> types) {
        for (var type : types)
            base &= ~type.getFlag();
        return base;
    }

    public static int setTo(int base, @NonNull EnemyInteractionType type, boolean set)
    {
        return set ? set(base, type) : clear(base, type);
    }

    public static int setTo(int base, boolean set, @NonNull EnemyInteractionType @NonNull ... types)
    {
        return set ? set(base, types) : clear(base, types);
    }

    public static int setTo(int base, @NonNull Collection<EnemyInteractionType> types, boolean set)
    {
        return set ? set(base, types) : clear(base, types);
    }

    public static boolean isSet(int base, @NonNull EnemyInteractionType type) { return (base & type.getFlag()) != 0; }
}
