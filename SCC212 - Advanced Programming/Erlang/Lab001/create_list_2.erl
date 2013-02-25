-module(create_list_2).
-export([create_list_2/1]).

build(0) ->	
	[];
build(N) ->
	[ N | build(N - 1) ].
create_list_2(N) ->
	lists:reverse(build(N)).