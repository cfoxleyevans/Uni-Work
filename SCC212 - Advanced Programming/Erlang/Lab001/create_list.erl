-module(create_list).
-export([create_list/1]).

build(0) ->	
	[];
build(N) ->
	[ N | build(N - 1) ].
create_list(N) ->
	build(N).