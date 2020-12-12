import sys
import json

def writeFile(summary, outputFileName):
    print("Creating summary file:", outputFileName)
    with open(outputFileName, 'w') as f:
        json.dump(summary, f, indent=2)  

def getMinorityIndex(minorityGroup):
    ethnicGroups = ["WHITE", "BLACK", "NATIVE_AMERICAN", "ASIAN", "PACIFIC_ISLANDER", "OTHER", "HISPANIC"]
    return ethnicGroups.index(minorityGroup) + 2

def modifyPrecinctProperties(props, adjacent, minorityGroup):
    newProps = {}
    newProps['precinct'] = props['PRECINCT']
    newProps['precinctID'] = props['ID']
    newProps['county'] = props['COUNTY']
    newProps['countyID'] = props['COUNTY_ID']
    newProps['population'] = props['TOTAL']
    newProps['votingAgePopulation'] = props['VAP_TOTAL']
    newProps['adjacentPrecincts'] = adjacent

    minorityIndex = getMinorityIndex(minorityGroup)
    newProps['minorityPopulation'] = props["T"+str(minorityIndex)]
    newProps['minorityVotingAgePopulation'] = props["R"+str(minorityIndex)]
    return newProps

def getPrecinctNeighbors(geoId, neighbors):
    for precinct in neighbors:
        if precinct['GEOID'] == geoId:
            return precinct['NEIGHBORS']
    return []

def getPrecinctsGeoJson(precincts_file_name, neighbors_file_name, minorityGroup):
    precincts = None
    with open(precincts_file_name) as f:
        precincts = json.load(f)

    neighbors = None
    with open(neighbors_file_name) as f:
        neighbors = json.load(f)

    i = 0
    for precinctGeo in precincts['features']:
        props = precinctGeo['properties']
        adjacent = getPrecinctNeighbors(props['ID'], neighbors)
        precincts['features'][i]['properties'] = modifyPrecinctProperties(props, adjacent, minorityGroup)
        i += 1

    return precincts

def modifyDistrictProperties(props, precincts, minorityGroup):
    newProps = {}
    newProps['district'] = "District"+str(props['ID'])
    newProps['districtID'] = props['ID']
    newProps['population'] = props['TOTAL']
    newProps['votingAgePopulation'] = props['VAP_TOTAL']

    minorityIndex = getMinorityIndex(minorityGroup)
    newProps['minorityPopulation'] = props['T'+str(minorityIndex)]
    newProps['minorityVotingAgePopulation'] = props['R'+str(minorityIndex)]
    newProps['differentCounties'] = props['UNIQUE_COUNTIES']
    newProps['adjacentDistricts'] = props['NEIGHBORS']
    newProps['precinctsInfo'] = precincts

    return newProps

def getDistrictPrecincts(districtId, districting):
    for district in districting['districts']:
        if district['id'] == districtId:
            return district['precincts']
    return []

def getDistrictGeoJson(geojsonFile, districting, minorityGroup):
    geo = None
    with open(geojsonFile) as f:
        geo = json.load(f)
    
    geo['description'] = 'Congressional Districts'
    i = 0
    for district in geo['features']:
        precincts = getDistrictPrecincts(district['properties']['INDEX'], districting)
        geo['features'][i]['properties'] = modifyDistrictProperties(district['properties'], precincts, minorityGroup)
        i += 1
    return geo

def getDistrictingsGeoJson(job_file_name, averageIndex, averageGeojson, extremeIndex, extremeGeojson, minorityGroup):
    averageOutput = {}
    extremeOutput = {}

    districtings = None
    with open(job_file_name) as f:
        districtings = json.load(f)

    averageDistricting = districtings[int(averageIndex)]
    extremeDistricting = districtings[int(extremeIndex)]

    averageOutput['districtingID'] = averageIndex
    extremeOutput['districtingID'] = extremeIndex
    averageOutput['congressionalDistrictsGeoJSON'] = getDistrictGeoJson(averageGeojson, averageDistricting, minorityGroup)
    extremeOutput['congressionalDistrictsGeoJSON'] = getDistrictGeoJson(extremeGeojson, extremeDistricting, minorityGroup)
    return [averageOutput, extremeOutput]

jobId = sys.argv[1]
state = sys.argv[2].lower()
compactness = sys.argv[3]
populationDeviation = sys.argv[4]
minorityGroup = sys.argv[5]
averageIndex = sys.argv[6]
extremeIndex = sys.argv[7]
outputFileName = sys.argv[8]
print(sys.argv)

compDic = {"0.75": "Slightly Compact", "0.5": "Moderately Compact", "0.25": "Very Compact"}
stateDic = {"Alabama": "AL", "Florida": "FL", "VIRGINIA": "VA"}
minorityDic = {"WHITE": "White American", "BLACK": "African American", "NATIVE_AMERICAN": "Native American and Alaska Native", "ASIAN": "Asian", "PACIFIC_ISLANDER": "Native Hawaiian and Other Pacific Islander", "OTHER": "Other", "HISPANIC": "Hispanic or Latino"}
constraints = {"compactnessLimit": compDic[compactness], "populationDifferenceLimit": populationDeviation, "minorityGroups": [minorityDic[minorityGroup]]}

precincts_file_name = "src/main/resources/precincts/"+state+"_precincts.json"
neighbors_file_name = "src/main/resources/algorithm_input/"+state+"_input.json"

job_file_name = "src/main/resources/jobs/"+jobId+"_districtings.json"
average_geojson = "src/main/resources/job_districts/"+jobId+"_average.json"
extreme_geojson = "src/main/resources/job_districts/"+jobId+"_extreme.json"

jobSummary = {"states": [{}]}

jobSummary['states'][0]['stateName'] = state.title()
jobSummary['states'][0]['stateId'] = stateDic[state.title()]

jobSummary['states'][0]['precinctsGeoJson'] = getPrecinctsGeoJson(precincts_file_name, neighbors_file_name, minorityGroup)
jobSummary['states'][0]['precinctsGeoJson']['description'] = state + " State Precincts"

jobSummary['states'][0]['averageDistricting'] = averageIndex
jobSummary['states'][0]['extremeDistricting'] = extremeIndex

jobSummary['states'][0]['constraints'] = constraints
jobSummary['states'][0]['districtings'] = getDistrictingsGeoJson(job_file_name, averageIndex, average_geojson, extremeIndex, extreme_geojson, minorityGroup)

writeFile(jobSummary, outputFileName)