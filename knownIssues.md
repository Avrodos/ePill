#Enter known issues in this file
-----
1.: 

Warning: validateDOMNesting(...): <th> cannot appear as a child of <thead>.
    in th (created by MedicationPlanView)
    in thead (created by MedicationPlanView)
    in table (created by MedicationPlanView)
    in div (created by MedicationPlanView)
    in div (created by MedicationPlanView)
    in div (created by MedicationPlanView)
    in MedicationPlanView (created by Translate(MedicationPlanView))
    in Translate(MedicationPlanView) (created by Route)
    in Route (created by Root)
    in Switch (created by Root)
    in div (created by Root)
    in Root
    in Router (created by HashRouter)
    in HashRouter
    in I18nextProvider
    in CookiesProvider react-dom.development.js:545
---
2.: remove all console.logs (before pushing)
---
3.: 
    status=200 medication_plan_view.js:165:17
    case status 200 medication_plan_view.js:170:25
    getting userdrugplanned medication_plan_view.js:36:16
    Error: Request failed with status code 500 createError.js:16
    recalculating user drug plan medication_plan_view.js:157:16
    status=200
---
4.: Weight doesnt save.
---
