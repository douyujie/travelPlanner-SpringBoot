package com.example.springdemo.models.entity;

import javax.persistence.*;
import java.io.Serializable;

@lombok.Data
@Embeddable
public class RouteAttractionId implements Serializable {
    private static final long serialVersionUID = 7551899649836522523L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "route_id", nullable = false, insertable = false, updatable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attraction_id", nullable = false, insertable = false, updatable = false)
    private Attraction attraction;

    @Override
    public int hashCode() {
        return (int)route.getId() + (int)attraction.getId();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RouteAttractionId) {
            RouteAttractionId otherId = (RouteAttractionId) object;
            return (otherId.route == this.route)
                    && (otherId.attraction == this.attraction);
        }
        return false;
    }
}
