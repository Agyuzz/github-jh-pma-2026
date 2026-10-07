# Hod kostkou – tipování sudé/liché (XML)

Jednoduchá Android aplikace v Kotlinu s klasickým XML layoutem. Hráč tipuje, zda na kostce padne sudé, nebo liché číslo, a aplikace sleduje jeho skóre.

## Funkce
- Tlačítka **Sudé** a **Liché** slouží k tipu a zároveň spustí hod.
- Animace hodu: 10 rychlých změn symbolu po 250 ms, poté se zobrazí výsledek.
- Kostka se zobrazuje jako Unicode symbol (⚀ ⚁ ⚂ ⚃ ⚄ ⚅).
- Po hodu se ukáže, co padlo, a zda byl tip správný.
- Skóre ve tvaru „trefy / počet hodů“.
- Tlačítka jsou během hodu zakázaná, takže nejde spustit více hodů naráz.

## Použité technologie
- Kotlin
- XML layout (`LinearLayout`, `TextView`, `Button`)
- `AppCompatActivity` a `findViewById`
- Coroutines (`lifecycleScope`, `delay`) pro animaci hodu
- Edge-to-edge zobrazení s ošetřením systémových lišt (`WindowInsets`)

## Jak spustit
1. Otevři projekt v Android Studiu.
2. Počkej na dokončení Gradle sync.
3. Spusť aplikaci na emulátoru nebo zařízení (`Run ▶`).

## Struktura
- `MainActivity.kt` – logika hry (hod, vyhodnocení tipu, skóre).
- `res/layout/activity_main.xml` – rozložení obrazovky.

## Autor
Jakub Hofman