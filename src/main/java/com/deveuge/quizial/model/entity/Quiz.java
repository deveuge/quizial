package com.deveuge.quizial.model.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.deveuge.quizial.util.FileUtils;
import com.deveuge.quizial.util.aspect.AuditableColumns;
import com.deveuge.quizial.util.aspect.AuditableEntity;
import com.deveuge.quizial.util.aspect.AuditableEntityListener;
import com.deveuge.quizial.util.aspect.PreservePostMerge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditableEntityListener.class)
public class Quiz extends AuditableColumns implements Serializable, AuditableEntity {
	
	private static final long serialVersionUID = -8935500629795822305L;

	@Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "longtext")
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column
    private String image;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("letter")
    private Set<Result> results;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Question> questions;
    
    @Transient
    @PreservePostMerge
    private transient MultipartFile imageFile;
    
    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "favorite", joinColumns = @JoinColumn(name = "quiz_id"), inverseJoinColumns = @JoinColumn(name = "user_username"))
	private Set<User> favoriteUsers;
    
    @PrePersist
    private void prePersist() {
    	if(this.imageFile != null) {
    		String filename = FileUtils.uploadQuizImage(imageFile);
			this.setImage(filename);
    	}
    }

    @PreUpdate 
    private void preUpdate() {
    	if(this.imageFile != null) {
    		if(StringUtils.hasText(this.image)) {
        		FileUtils.removeQuizImage(this.image);
        	}
    		String filename = FileUtils.uploadQuizImage(imageFile);
			this.setImage(filename);
    	}
    }
    
    @PreRemove
    private void preRemove() {
    	if(StringUtils.hasText(this.image)) {
    		FileUtils.removeQuizImage(this.image);
    	}
    }
    
    @Override
	public String getAuditableId() {
		return String.valueOf(id);
	}
    
}
