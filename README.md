# Solar Power Calculator (Console) 

Simple console app para i-estimate ang daily kuryente mo, i-setup basic solar (panel + battery), at i-compute ang babayaran mo sa electric provider kung kulang ang solar mo. Chill, newbie-friendly to. 👌

## Modes (Dalawang paraan)
- [1] Electricity bill calculator — ito yung original flow na may receipt at final bill after solar.
- [2] Sizing calculator — bagong feature na nagco-compute ng REQUIRED sizes: solar panel (W), battery (Ah), inverter (W), at charge controller (A).

## Overview 
- Set mo yung electricity rate (pesos/kWh) — para legit ang bill.
- Pili ka ng solar panel size, dami (quantity), at ilang oras kada araw.
- Setup battery: voltage (V), capacity (Ah), at dami.
- Add appliances/devices: watts, ilang oras ginagamit per day, at ilan (quantity).
- Makakakuha ka ng “receipt” table na may per-device energy (Wh), totals, at final bill after solar.

## Data Flow 
1) Start: Hihingi ng electricity rate (pesos/kWh). May default, pwede mo palitan.
2) Solar Panel: Pili ng size → set quantity → set sunlight hours. App computes power (W) at daily energy (Wh/day).
3) Battery: Pili ng voltage at Ah → set quantity. App computes battery energy (Wh).
4) Devices: Pili from menu → ilagay watts, hours per day, at quantity. App adds energy = watts × hours × quantity.
5) Receipt: Lalabas table + totals (Wh/kWh), base bill, solar energy, grid deficit, at final bill (daily/monthly) after solar.

## Sizing Calculator (Bagong feature)
Pag pinili mo ang Mode [2], hihingi ito ng:
- Peak Sun Hours (PSH) — ilang oras tunay na maaraw per day.
- System voltage — 12V / 24V / 48V.
- Depth of Discharge (DoD %) — ilang porsyento ng battery ang pwede gamitin (e.g., 50).
- Days of Autonomy — ilang araw na backup gusto mo.
- Devices — same input: watts, hours/day, quantity.

Outputs na ipi-print:
- Total Daily Load (Wh/day at kWh/day)
- Required Solar Panel size (W)
- Required Battery capacity (Ah @ system voltage, with total Wh)
- Recommended Inverter size (W)
- Recommended Charge Controller size (A)

Ginamit naming simple, readable formulas (may safety/derating):
- Panel derating = 0.8 (80%)
- Inverter safety = 1.25× (based sa approx peak = sum of watts × quantity)
- Controller size ≈ (panelW / systemV) × 1.25
- Battery required Wh = (daily load × days) / DoD

## Core Computations    
- Energy per device per day: `Wh = watts × hours × quantity`
- Total load per day: add mo lahat ng Wh ng devices
- kWh: `kWh = Wh / 1000` (ito ang gamit sa bill)
- Battery energy: `Wh = V × Ah × dami`
- Solar power: `W = V × A × dami`
- Solar daily energy: `Wh/day = W × sunlightHours`
- Final bill (deficit lang ang babayaran):
  - `deficitWh = max(totalLoadWh − solarWh, 0)`
  - `finalDailyBill = (deficitWh / 1000) × ratePerKWh`
  - `finalMonthlyBill = finalDailyBill × 30`

---

## Step-by-step: Mode [1] Electricity Bill Calculator
1) Enter rate (pesos/kWh).
2) Setup panels (size, quantity, sunlight hours).
3) Setup battery (voltage, Ah, quantity).
4) Add devices:
   - Choose device → input watts, hours/day, quantity.
   - Repeat for all appliances → choose [7] End.
5) View receipt:
   - Table per device (Watts, Hours, Qty, EnergyWh)
   - Total daily consumption (Wh/kWh)
   - Base bill (kung walang solar)
   - After Solar: solar energy, grid deficit, final daily/monthly bill.

Mini example
- TV 100W × 3h × 2 = 600 Wh
- Fan 40W × 5h × 1 = 200 Wh
- Total load = 800 Wh = 0.8 kWh
- If solar = 500 Wh → deficit = 300 Wh = 0.3 kWh
- Rate = 14/kWh → final daily bill = 0.3 × 14 = 4.20 pesos

---

## Step-by-step: Mode [2] Sizing Calculator
Inputs you’ll enter
- PSH (Peak Sun Hours) → ilang oras tunay na maaraw, e.g., 4–5
- System Voltage → 12V, 24V, 48V (mas mataas = mas maliit na current)
- DoD% (Depth of Discharge) → percent ng battery na pwede mong gamitin (e.g., 50%)
- Days of Autonomy → ilang araw na kaya ng battery kung walang araw (e.g., 2 days)
- Devices → watts, hours/day, quantity (same as Mode 1)

Outputs you’ll get
- Required panel W 
- Required battery Ah @ chosen voltage 
- Recommended inverter W 
- Recommended charge controller A

Mini example 
- Total daily load = 3,000 Wh
- PSH = 4 h → Required panel W ≈ 3000 / (4 × 0.8) = 937.5 W → ~ 938 W
- System V = 24V, DoD = 50%, Days = 1 → Required battery Wh ≈ (3000 × 1) / 0.5 = 6000 Wh
- Battery Ah ≈ 6000 / 24 = 250 Ah @ 24V
- Approx peak (sum watts × qty) = 900 W → inverter ≈ 900 × 1.25 = 1125 W → ~1.2 kW
- Controller A ≈ (panelW / systemV) × 1.25 = (938 / 24) × 1.25 ≈ 48.9 A → ~50 A

Tip: Gusto mo ng "number of panels"? Kung 350W/panel → 938/350 ≈ 2.68 → round up to 3 panels.

---

## Para klaro yung terms
- W (Watt) → power, parang instantaneous lakas.
- Wh (Watt-hour) → energy per day sa app, watts × hours.
- kWh → Wh ÷ 1000, ito ang ginagamit sa bill.
- PSH (Peak Sun Hours) → effective na oras na maaraw .
- DoD (Depth of Discharge) → ilang percent ng battery ang pwede mong gamitin safely.
- Days of Autonomy → ilang araw tatagal ang battery kung walang araw.

---

## Study tips
- Huwag kabisaduhin lahat ng formula—intindihin lang: load → solar covers part → kulang = babayaran.
- Start with rough numbers, then fine-tune.
- Always round up for sizing (para may allowance).

Notes:
- Wh = energy; W = power. Battery “Current” dito ay Ah (capacity).

## Files and Their Jobs 
- Main.java — Flow controller. Dito nangyayari ang prompts at summary. Tumatawag ng `displayReceiptWithSolar(...)` para sa full receipt.
- ElectronicDevices.java — Dito naka-list ang devices at defaults. Nagre-record ng usage (`addUsageCustom(..., quantity)`) at nagco-compute ng totals at bills. Siya rin nagpi-print ng receipt table.
- Battery.java — May `volt (V)` at `current (Ah)`. Kinocompute ang energy (Wh) at may bordered display.
- SolarBattery.java — Battery na may presets (12.8/25.6/51.2 V, iba’t ibang Ah) at quantity.
- EnergySource.java — Base ng mga power source (may V at A) at `getPower()` (W). May bordered display.
- SolarPanel.java — Extends EnergySource; may quantity at sunlight hours. May `getRatedPowerW()` at `getDailyEnergyWh()`.
- SolarInverter.java / SolarChargeController.java — Future use (placeholder pa).

## Console UX 
- May banners/sections para hindi ka maligaw.
- Validation para hindi ka makapaglagay ng negative o out-of-range.
- Battery at Solar info naka-bordered block (malinis tingnan).
- Receipt naka-table na may columns: `#`, `Device`, `Watts(W)`, `Hours`, `Qty`, `EnergyWh`.

## Configuration 
- Electricity rate (pesos/kWh): hinihingi sa start, pwede mong palitan kada run.
- Device watts/hours/quantity: ikaw bahala, may default guide lang.

## Notes / Limitations 
- Walang inverter/controller/wire losses (for now).
- Flat rate lang (walang peak/off-peak).
- Battery as bucket: walang DoD o efficiency pa.
- Solar output = rated W × sunlight hours (no weather derating).
- Device list maliit, pero pwede ka mag-custom values.

## Recommendations 
- Add efficiency: panel derating, inverter/controller losses, battery DoD/efficiency.
- Mas flexible devices: custom names, save recent, optional duty cycle (ref: ref 60%).
- UX: optional colors, clear screen per section, naka-center banners, final summary block.
- Save settings: rate/panel/devices sa config file.
- Guardrails: warnings pag sobrang laki ng watts/hours.

## Build & Run 
- Kailangan Java 8+.
- Sa project folder:
  - Compile: `javac *.java`
  - Run: `java Main`

## Example Summary 
- Battery capacity: 11,520 Wh
- Solar daily energy: 7,200 Wh
- Total daily load: 9,000 Wh (9.00 kWh)
- Grid deficit: 1,800 Wh (1.80 kWh)
- Final Daily Bill (@ 13.85/kWh): 24.93 pesos
- Final Monthly Bill: 747.90 pesos
