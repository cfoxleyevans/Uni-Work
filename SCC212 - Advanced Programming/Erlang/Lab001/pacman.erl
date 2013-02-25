-module(pacman).
-export([pacman/1]).

pacman({ate_pill, ate_fruit}) -> bonus;
pacman({ate_fruit, ate_pill}) -> bonus;
pacman({ate_pill}) -> score;
pacman({ate_fruit}) -> score;
pacman(_) -> no_score.