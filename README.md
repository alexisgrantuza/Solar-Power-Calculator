# Solar Power Calculator (Console) 

Simple console app para i-estimate ang daily kuryente mo, i-setup basic solar (panel + battery), at i-compute ang babayaran mo sa electric provider kung kulang ang solar mo. Chill, newbie-friendly to. 👌

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
