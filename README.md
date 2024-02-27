## Baccarat Game Simulator

Created a simple application mimicking the game commonly played during chinese new year and in casinos, based on simplified play rules for casinos!

## Game Commands & Game Set-Up
<pre>
args[] on client side should be localhost:12345       (noting that 12345 is port number)
args[] on server side should be 12345 2               (noting that 12345 is port number and 2 is the number of decks to be used in the game)

login username 1234                                   (login is the keyword, username and 1234 can be switched for desired username and account value)
login username                                        (login to existing user without changing account value)
quit                                                  (end the session)
</pre>
## Key Practise Areas

- Simple Concurrency (without dealing with race conditions)
- Sockets & IO Handling (properly implementing reader .readLine() and **FULLY** understanding its behaviour and side effects when used on a stream)
