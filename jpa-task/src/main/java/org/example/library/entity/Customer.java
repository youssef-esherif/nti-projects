package org.example.library.entity;

import javax.persistence.*;

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends Person {

    @Column(name = "membership_no")
    private String membershipNo;

    public Customer() {}

    public Customer(String name, String membershipNo) {
        super(name);
        this.membershipNo = membershipNo;
    }

    public String getMembershipNo() { return membershipNo; }
    public void setMembershipNo(String membershipNo) { this.membershipNo = membershipNo; }
}