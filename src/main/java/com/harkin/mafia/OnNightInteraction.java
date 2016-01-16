package com.harkin.mafia;

import com.harkin.mafia.models.Role;

import java.util.List;

public interface OnNightInteraction {
    void onNightEnd(List<Role> theMurdered);
}
