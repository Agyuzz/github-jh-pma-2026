# Hod kostkou (Jetpack Compose)

Jednoduchá Android aplikace v Kotlinu a Jetpack Compose, která simuluje hod šestistěnnou kostkou.

## Funkce
- Tlačítko **Hodit** spustí animaci hodu (10 rychlých změn hodnoty po 250 ms) a poté zobrazí výsledek.
- Kostka se zobrazuje jako Unicode symbol (⚀ ⚁ ⚂ ⚃ ⚄ ⚅).
- Počítadlo celkového počtu hodů.
- Statistika, kolikrát padla jednotlivá hodnota (1–6).
- Tlačítko je během hodu zakázané, takže nejde spustit více hodů naráz.

## Použité technologie
- Kotlin
- Jetpack Compose (Material 3)
- Stav přes `remember` / `mutableStateOf`
- Coroutines (`rememberCoroutineScope`, `delay`) pro animaci hodu

## Jak spustit
1. Otevři projekt v Android Studiu.
2. Počkej na dokončení Gradle sync.
3. Spusť aplikaci na emulátoru nebo zařízení (`Run ▶`).

## Autor
Jakub Hofman