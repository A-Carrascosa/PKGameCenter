package com.example.gamecenter.gc2048;

public interface GameGestureDetector {
    public void addSwipeGestureListener(SwipeGestureListener listener);
    public void removeSwipeGestureListener(SwipeGestureListener listener);
    public void clearSwipeGestureListeners();
}
