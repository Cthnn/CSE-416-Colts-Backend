import json
import math
from shapely.geometry import Polygon, mapping

def getPolygon(geo):
    polygon = None
    if(geo != None):
        if(geo['type'] == 'MultiPolygon'):
            for poly in geo['coordinates']:
                if(polygon == None):
                    polygon = Polygon(poly[0])
                else:
                    polygon = polygon.union(Polygon(poly[0]))
        else:
            polygon = Polygon(geo['coordinates'][0])
    return polygon

def polygonsToGeoJson(districts, districtProperties):
    geoJson = []
    for districtId in districts.keys():
        district = {"type": "Feature", "geometry": mapping(districts[districtId]), "properties": districtProperties[districtId]}
        geoJson.append(district)

    return geoJson


def writeFile(state, precincts, output_file_name):
    if len(precincts) > 0:
        print("Creating precinct geojson file:", output_file_name)
        with open(output_file_name, 'w') as error_file:
            output_json = {"type": "FeatureCollection", "features": precincts}
            json.dump(output_json, error_file, indent=2)   

def getPrecinctGeo(precinctID, precinctGeoJson):
    for precinct in precinctGeoJson:
        if(precinctID == precinct['properties']['ID']):
            return precinct['geometry'], precinct['properties']

    return None

def initProperties(districtId):
    properties = {}
    properties["ID"] = districtId
    properties["TOTAL"] = 0
    properties["T2"] = 0
    properties["T3"] = 0
    properties["T4"] = 0
    properties["T5"] = 0
    properties["T6"] = 0
    properties["T7"] = 0
    properties["T8"] = 0
    properties["VAP_TOTAL"] = 0
    properties["R2"] = 0
    properties["R3"] = 0
    properties["R4"] = 0
    properties["R5"] = 0
    properties["R6"] = 0
    properties["R7"] = 0
    properties["R8"] = 0
    return properties


def addDemographicData(properties, geoProps):
    properties["TOTAL"] += geoProps["TOTAL"]
    properties["T2"] += geoProps["T2"]
    properties["T3"] += geoProps["T3"]
    properties["T4"] += geoProps["T4"]
    properties["T5"] += geoProps["T5"]
    properties["T6"] += geoProps["T6"]
    properties["T7"] += geoProps["T7"]
    properties["T8"] += geoProps["T8"]
    properties["VAP_TOTAL"] += geoProps["VAP_TOTAL"]
    properties["R2"] += geoProps["R2"]
    properties["R3"] += geoProps["R3"]
    properties["R4"] += geoProps["R4"]
    properties["R5"] += geoProps["R5"]
    properties["R6"] += geoProps["R6"]
    properties["R7"] += geoProps["R7"]
    properties["R8"] += geoProps["R8"]


state = 'virginia'
print("Creating District geojson for", state)

inputFileName = "results.json"
precinctGeoJsonFileName = state+"_precincts.json"

districtings = None
with open(inputFileName) as f:
    districtings = json.load(f)

precinctGeoJson = None
with open(precinctGeoJsonFileName) as f:
    precinctGeoJson = json.load(f)['features']

for num in range(len(districtings)):
    outputFileName = str(num)+"_districts.json"

    districtPolygons = {}
    districtProperties = {}

    districts = districtings[num]['districts']

    for district in districts:
        districtId = district['id']
        districtPrecincts = district['precincts']
        districtPolygon = None
        properties = initProperties(districtId)
        for precinctId in districtPrecincts:
            geo, geoProps = getPrecinctGeo(precinctId, precinctGeoJson)
            if(geo == None):
                print("Error: no geometry for precinct", precinctId)
            else:
                if(districtPolygon == None):
                    districtPolygon = getPolygon(geo)
                else:
                    districtPolygon = districtPolygon.union(getPolygon(geo))
                addDemographicData(properties, geoProps)
        districtPolygons[districtId] = districtPolygon
        districtProperties[districtId] = properties
    writeFile(state, polygonsToGeoJson(districtPolygons, districtProperties), outputFileName)