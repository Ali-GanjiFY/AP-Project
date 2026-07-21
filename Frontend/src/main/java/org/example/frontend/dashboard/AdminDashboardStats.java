package org.example.frontend.dashboard;

/**
 * Represents admin dashboard stats.
 */
public class AdminDashboardStats {

    private long usersCount;
    private long advertisementsCount;
    private long adminReviewsCount;
    private long pendingAdvertisementsCount;

    /**
     * Gets users count.
     * @return the result
     */
    public long getUsersCount() {
        return usersCount;
    }

    /**
     * Sets users count.
     * @param usersCount the users count
     */
    public void setUsersCount(long usersCount) {
        this.usersCount = usersCount;
    }

    /**
     * Gets advertisements count.
     * @return the result
     */
    public long getAdvertisementsCount() {
        return advertisementsCount;
    }

    /**
     * Sets advertisements count.
     * @param advertisementsCount the advertisements count
     */
    public void setAdvertisementsCount(long advertisementsCount) {
        this.advertisementsCount = advertisementsCount;
    }

    /**
     * Gets admin reviews count.
     * @return the result
     */
    public long getAdminReviewsCount() {
        return adminReviewsCount;
    }

    /**
     * Sets admin reviews count.
     * @param adminReviewsCount the admin reviews count
     */
    public void setAdminReviewsCount(long adminReviewsCount) {
        this.adminReviewsCount = adminReviewsCount;
    }

    /**
     * Gets pending advertisements count.
     * @return the result
     */
    public long getPendingAdvertisementsCount() {
        return pendingAdvertisementsCount;
    }

    /**
     * Sets pending advertisements count.
     * @param pendingAdvertisementsCount the pending advertisements count
     */
    public void setPendingAdvertisementsCount(long pendingAdvertisementsCount) {
        this.pendingAdvertisementsCount = pendingAdvertisementsCount;
    }
}

