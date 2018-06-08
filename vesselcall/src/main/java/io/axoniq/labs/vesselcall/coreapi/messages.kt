package io.axoniq.labs.vesselcall.coreapi

import org.axonframework.commandhandling.TargetAggregateIdentifier

data class DeclareVisit2PaCommand(@TargetAggregateIdentifier val crn: String)
data class Visit2PADeclaredEvent(val crn: String, val visit: Visit)

data class PaApprovedCommand(@TargetAggregateIdentifier val crn: String)
data class PaApprovedEvent(val crn: String)
data class PaRejectedCommand(@TargetAggregateIdentifier val crn: String, val reason: String)
data class PaRejectedEvent(val crn: String, val reason: String)


data class CreateVisitCommand(@TargetAggregateIdentifier val crn: String, val vessel: String, val port: String, val accessPoint: String)
data class VisitCreatedEvent(val crn: String, val vessel: String, val port: String, val accessPoint: String)

data class SaveVisitCommand(@TargetAggregateIdentifier val crn: String, val accessPoint: String)
data class GetVisitCommand(@TargetAggregateIdentifier val crn: String)
data class VisitSavedEvent(val crn: String, val accessPoint: String)

data class AddBerthCommand(@TargetAggregateIdentifier val crn: String, val berth: String)
data class BerthAddedEvent(val crn: String, val berth: String)

data class DeleteBerthCommand(@TargetAggregateIdentifier val crn: String, val berth: String)
data class BerthDeletedEvent(val crn: String, val berth: String)

data class CreateWasteCommand(@TargetAggregateIdentifier val crn: String, val wasteItems : List<WasteItem>)
data class GetWasteCommand(@TargetAggregateIdentifier val crn: String)
data class WasteCreatedEvent(val crn: String, val wasteCommand: CreateWasteCommand)

data class SaveWasteCommand(@TargetAggregateIdentifier val crn: String, val wasteItems : List<WasteItem>)
data class WasteSavedEvent(val crn: String, val wasteCommand: SaveWasteCommand)

data class Waste2SingleWindowCommand(@TargetAggregateIdentifier val crn: String)
data class Waste2SingleWindowDeclaredEvent(val crn: String, val waste: Waste)


// PortAuthority
data class CreatePortAuthorityCommand(@TargetAggregateIdentifier val portId: String)
data class PortAuthorityCreatedEvent(val portId: String)
