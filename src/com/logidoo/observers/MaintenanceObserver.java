package com.logidoo.observers;

import com.logidoo.models.MaintenanceTicket;

/**
 * Observer interface for maintenance ticket notifications.
 */
public interface MaintenanceObserver {
    void onTicketCreated(MaintenanceTicket ticket);
}
