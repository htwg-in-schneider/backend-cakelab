package cakelab.backend.dto;

public class ReviewResponseDto {

    private Long id;
    private int stars;
    private String text;
    private String username;

    public ReviewResponseDto(Long id, int stars, String text, String username) {
        this.id = id;
        this.stars = stars;
        this.text = text;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public int getStars() {
        return stars;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }
}
