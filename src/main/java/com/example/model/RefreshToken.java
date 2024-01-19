package com.example.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "refreshtoken")
public class RefreshToken {
	
	  @Id
	  @GeneratedValue(strategy = GenerationType.AUTO)
	  private long id;

	  @OneToOne
	  @JoinColumn(name = "user_id", referencedColumnName = "id")
	  private User user;

	  @Column(nullable = false, unique = true)
	  private String tokenId;

	  @Column(nullable = false)
	  private Instant expiryDate;

}
