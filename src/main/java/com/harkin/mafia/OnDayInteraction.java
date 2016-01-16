package com.harkin.mafia;

import com.harkin.mafia.models.Role;

public interface OnDayInteraction {
    //todo day end with kill/ day end without kill might be clearer
    void onDayEnd(Role role);
}
