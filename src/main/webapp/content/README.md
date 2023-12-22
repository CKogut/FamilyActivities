# vanillaJS notes

what is the URL for these pages?

`localhost:8080/notes.html`

(or whatever PORT you have your jhipster REST API running on)

what was the BEAN disable that dropped the security on the app?

SecurityConfiguration.java

```
    .antMatchers("/api/**").permitAll() // comment out .authenticated()
```

(this is the code that allows the app's api to be accessed without a login)
