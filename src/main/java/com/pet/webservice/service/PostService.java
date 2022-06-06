package com.pet.webservice.service;

import com.pet.webservice.dto.PostDTO;
import com.pet.webservice.entity.ImageModel;
import com.pet.webservice.entity.Post;
import com.pet.webservice.entity.User;
import com.pet.webservice.exceptions.PostNotFoundException;
import com.pet.webservice.repository.ImageRepository;
import com.pet.webservice.repository.PostRepository;
import com.pet.webservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, ImageRepository imageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);
        post.setDislikes(0);

        LOG.info("Saving post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long postId, Principal principal) {
        User user = getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(postId, user)
                .orElseThrow(() -> new PostNotFoundException("Post can't be found for user: " + user.getEmail()));
    }

    public List<Post> getAllPostForUser(Principal principal) {
        User user = getUserByPrincipal(principal);

        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post can't be found"));

        Optional<String> userLiked = post.getLikedUsers()
                .stream()
                .filter(user -> user.equals(username)).findAny();

        /* if user liked post, when he's trying again, service delete he's like
        * and remove he from LikedUser List, otherwise, a like is added to the post and the user is
        * added to LikedUser List*/
        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes()- 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }

        return postRepository.save(post);
    }

    public Post dislikePost(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post can't be found"));

        Optional<String> userDisliked = post.getDislikedUsers()
                .stream()
                .filter(user -> user.equals(username)).findAny();

        if (userDisliked.isPresent()) {
            post.setDislikes(post.getDislikes() - 1);
            post.getDislikedUsers().remove(username);
        } else {
            post.setDislikes(post.getDislikes() + 1);
            post.getDislikedUsers().add(username);
        }

        return postRepository.save(post);
    }

    public void deletePost(Long postId, Principal principal) {
        Post post = getPostById(postId, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();

        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username with " + username + " username wasn't found"));
    }
}
